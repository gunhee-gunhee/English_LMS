package com.english.lms.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class ZoomTokenServiceImpl implements ZoomTokenService {
	
	/*
	 * Zoom のアクセストークン発行・再利用
	 * 
	 * アクセストークンは「ちょうど1時間」まで再利用する
	 * ・有効期限の1分前からは新しいトークンを取得して使用する
	 * */
	
	//発行されたアクセストークン（初期値は null）
	private String accessToken = null;
	
	//アクセストークンの有効期限
	private long tokenExpiresAt = 0;
	
	//機密情報：ZoomアプリのアカウントID、クライアントID、クライアントシークレット（公開厳禁）
	//application.properties から @Value で読み込んで使用
	@Value("${zoom.account-id}")
	private String accountId;
	@Value("${zoom.client-id}")
	private String clientId;
	@Value("${zoom.client-secret}")
	private String clientSecret;
	
	/*
	 * トークンが有効であれば、そのまま return
	 * 有効期限が切れている場合は新しく発行し、期限を更新してから return
	 * */
	
	@Override
	public synchronized String getAccessToken() {
		// 現在時刻（ミリ秒単位
				long now = System.currentTimeMillis(); 
				
				//トークン再利用の条件
				//トークンが存在していて、有効期限まで1分（60,000ms）以上残っていればOK
				if(accessToken != null && now < tokenExpiresAt - 60000) {
					//すぐ return
					return accessToken;
				}
				
				//トークンを新規発行
				// Zoom OAuth トークン発行用APIのURL（常に固定）
				String url = "https://zoom.us/oauth/token";
				
				// Basic認証用の文字列を作成（clientId:clientSecret を Base64 でエンコード）
				String credentials = Base64.getEncoder().encodeToString(
						(clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)
						);
				
				//HTTPリクエストヘッダーの設定（フォームデータ + Basic認証）
				HttpHeaders headers = new HttpHeaders();
				
				//リクエストボディの Content-Type を指定する
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				//認証情報の設定
				headers.set("Authorization", "Basic "+ credentials);
				
				// ボディ（フォームデータ）の準備（grant_type, account_id など）
				MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
				
				// Server-to-Server方式の固定値
				params.add("grant_type", "account_credentials");
				params.add("account_id", accountId);
				
				// ヘッダー＋ボディを一つにまとめる
				HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
				
				// Zoom サーバーに POST リクエストを送る準備
				RestTemplate restTemplate = new RestTemplate();
				
				// Zoom サーバーにトークン発行リクエストを送信
				ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, request,  new ParameterizedTypeReference<Map<String, Object>>() {});
				
				// レスポンスから access_token と expires_in（有効期限：秒単位）を取り出す
				Map<String, Object> body = response.getBody();
				this.accessToken = body.get("access_token").toString(); // 新しいトークンを保存
				int expiresIn = Integer.parseInt(body.get("expires_in").toString()); // 有効期限までの残り時間（秒）
			
				// 有効期限を計算して保存！（現在時刻 + expiresIn 秒）
				this.tokenExpiresAt = now + expiresIn * 1000L;
				
				// トークンを返却
				return this.accessToken;
			}


}
