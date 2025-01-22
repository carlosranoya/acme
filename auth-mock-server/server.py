import json
import sys

from flask import Flask, request

cli = sys.modules['flask.cli']
cli.show_server_banner = lambda *x: None

key = "qwertyuiopasdfghjklzxcvbnm123456"

tokens = [
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBY21lIEF1dGgiLCJpYXQiOjE3MzczMTM4NjksImV4cCI6MTc2ODg0OTg2OSwiYXVkIjoid3d3LmFjbWUuY29tIiwic3ViIjoianJvY2tldEBhY21lLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAYWNtZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.jGvMFywDJNL12JuEaXSfnQpwp60o79RXj0aOh1WC6CI",
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBY21lIEF1dGgiLCJpYXQiOjE3MzczMTM4NjksImV4cCI6MTc2ODg0OTg2OSwiYXVkIjoid3d3LmFjbWUuY29tIiwic3ViIjoibWFndWlsYUBhY21lLmNvbSIsIkdpdmVuTmFtZSI6Ik1hZ3VpbGEiLCJTdXJuYW1lIjoiUm9kcmlndWVzIiwiRW1haWwiOiJtYWd1aWxhQGFjbWUuY29tIiwiUm9sZSI6IkNsaWVudCJ9.HcOhnY_4YYpJvSOGBAq-xPcASn4NY-QZrXAKsHB5D5s",
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBY21lIEF1dGgiLCJpYXQiOjE3MzczMTM4NjksImV4cCI6MTc2ODg0OTg2OSwiYXVkIjoid3d3LmFjbWUuY29tIiwic3ViIjoiY2FuZXRhLmF6dWxAYWNtZS5jb20iLCJHaXZlbk5hbWUiOiJDYW5ldGEiLCJTdXJuYW1lIjoiQXp1bCIsIkVtYWlsIjoiY2FuZXRhLmF6dWxAYWNtZS5jb20iLCJSb2xlIjoiQ2xpZW50In0.qOmVY1r9wSod4h0-ur8v2v8K1tkDQ1FHmAOIBcIWhqI"
]

app = Flask(__name__)

@app.route('/api/auth/validate', methods=['POST'])
def api_auth_validate():
    request_data = request.get_json()
    token = request_data['access_token']
    if token in tokens:
        return "", 204
    else:
        return "", 403


if __name__ == '__main__':
    port = sys.argv[1]
    app.run(debug=False, port=port, host="0.0.0.0")