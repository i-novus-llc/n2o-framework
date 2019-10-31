### Как комментировать код с помощью jsdoc

1. Описывать тип каждого свойства в propTypes.
```js
static propTypes = {
    /** @type {string} this does XYZ */
    propName: PropTypes.string, // simple
    /** @type {object} This is the profile data containing user data */
    profileData: PropTypes.object, // nice!
    /** @type {bool} If true, the component should be in a loading state */
    isLoading: PropTypes.bool, // looks like a bool to me!
    /** @type {array} items being passed in as an array */
    items: PropTypes.array, // it's plural, looks like an array
    /** @type {function} Function triggered when component is clicked */
    onClick: PropTypes.function, // on nice, this is DOM. I know this
    /** @type {function} Function triggered when CustomAction fired inside component */
    onCustomAction: PropTypes.function, // looks like a custom event that takes a handler!
  }
```
2. render() можно не документировать.

### Как генерировать документацию к коду из Jsdoc

Документация к коду генерируется с помощью [esdoc](https://esdoc.org)

Конфигурация в файле .esdoc.json.

```shell
$ yarn esdoc                    # Сгенерировать документацию по исходникам из папки по умолча
```

Документация появится в папке docs/esdoc.
Для просмотра достаточно открыть файл docs/esdoc/index.html.
Главная страница - файл README.md.
На вкладке Source можно увидеть процент покрытия кода.

