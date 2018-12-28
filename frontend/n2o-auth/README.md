# n2o-auth

### Содержимое пакета:
* authProvider

### Установка
```npm install n2o-auth```

### Использование
#### authProvider
Пример импользовнаия стандартного провайдера:
```
import React from 'react';
import ReactDOM from 'react-dom';
import N2O from 'n2o';
import { authProvider } from 'n2o-auth';

const config = {
  security: {
    authProvider,
    externalLoginUrl: '/'
  }
};

ReactDOM.render(<N2O {...config} />, document.getElementById('n2o'));
```

