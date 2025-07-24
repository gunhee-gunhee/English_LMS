package com.english.lms.service;

public interface ZoomTokenService {

    /**
     * Zoomアクセストークンを取得する。
     * もし期限切れなら自動的に再発行される。
     * @return アクセストークン
     */
    String getAccessToken();
}

