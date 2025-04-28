
CREATE TABLE api_audit_log (
    id SERIAL PRIMARY KEY,
    request_method VARCHAR(10) NOT NULL,
    request_parameters TEXT NOT NULL,
    request_url TEXT NOT NULL,
    request_headers TEXT,
    request_body TEXT,
    response_status INT NOT NULL,
    response_headers TEXT,
    response_body TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);