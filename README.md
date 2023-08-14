# ss-1

Общий принцип работы:
1. Передать JSON вида:
{"username" : "admin", "password" : 100}
По http://localhost:9000/auth
2. Получить JWT token, с ним ходить по другим url:
/info/admin
/resource/{id}
и тд

Для записи в БД используется /resource
Пример json для отправки:
{"id":1,"value":"value","path":"path"}

Пользователи по умолчанию:
user (роль USER), admin (роль ADMIN), у обоих пароль 100
