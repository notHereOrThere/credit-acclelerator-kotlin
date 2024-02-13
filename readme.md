Запросы проходят через gateway MC порядок вызов операций:

put: /application/offer { "applicationId": 2, "requestedAmount": 300000, "totalAmount": 314500, "term": 15, "monthlyPayment": 22246.59, "rate": 9, "isInsuranceEnabled": true, "isSalaryClient": true }

post: /application { "amount": 300000, "term": 15, "firstName": "Ivan", "lastName": "Ivanov", "middleName": "Ivanovich", "email": "ivan@gmail.com", "birthdate": "2000-01-01", "passportSeries": "1234", "passportNumber": "123456" }

put: /deal/calculate/{applicationId}

post: /deal/document/{applicationId}/send

post: /deal/document/{applicationId}/code

также в заголвки запросов необходимо добавлять jwt
для этого надо зарегистроваться по 
post: /auth/register {"username":..., "password": ...}
или залогиниться по 
post: /auth/login {"username":..., "password": ...}
