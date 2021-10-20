import React from "react";
import config from '../../docusaurus.config'

export const features = [
    {
        title: 'Реактивный',
        imageUrl: 'img/pic1_light.svg',
        description: (
                <>
                    Создавать интерактивные приложения на N2O можно легко и быстро как в конструкторе.
                    Для этого Вам не потребуются глубокие знания языков программирования.<br /><br />
                    Разработка происходит в декларативном стиле на языке XML.
                    Подключение к данным легко реализуется через SQL, REST, Spring и другие технологии.
                </>
        ),
    },
    {
        title: 'Мощный',
        imageUrl: 'img/pic2_light.svg',
        description: (
                <>
                    N2O имеет встроенную библиотеку React компонентов с настраиваемой вёрсткой и поведением.
                    С их помощью Вы можете компоновать страницу как хотите.<br /><br />
                    N2O поддерживает условия видимости и доступности полей, кнопок, панелей; фильтрации, сортировки данных;
                    открытия ссылок, вложенных страниц, модальных окон; валидации, сохранения и печати данных.
                </>
        ),
    },
    {
        title: 'Открытый',
        imageUrl: 'img/pic3_light.svg',
        description: (
                <>
                    N2O Framework - библиотека с открытым исходным кодом, Вы можете использовать её свободно в любых проектах.<br /><br />
                    N2O - компонентный фреймворк и его можно расширить под любые требования: создать свою тему стилей;
                    добавить специфические React компоненты; реализовать новый способ получения данных.<br /><br />
                    В N2O есть механизмы быстрого подключения системы авторизации, журналирования, аудита и отчетов.
                </>
        ),
    },
]

export const tabsValues = [
    {label: 'Пример первый', value: 'first'},
    {label: 'Пример второй', value: 'second'},
    {label: 'Пример третий', value: 'third'},
]

export const projectIds = {
    first: 'examples_greeting',
    second: 'examples_crud_sql',
    third: 'examples_fields'
}

const { items } = config.themeConfig.navbar
const cardsParams = {
    "Документация": {
        icon: 'img/docs.svg',
        href: '/docs'
    },
    "Примеры": {
        icon: 'img/examples.svg',
        href: '/docs'
    },
    "Песочница": {
        icon: 'img/sandbox.svg',
        href: '/docs'
    },
    "Блог": {
        icon: 'img/blog.svg',
        href: '/blog'
    },
}

export const cardItems = items.filter(item => cardsParams[item.label]).map(item => {
    const { label, href, docId } = item
    let currentHref = ''

    if (href) {
        currentHref = href
    } else if (docId === 'introduction' || label === 'Блог') {
        currentHref = cardsParams[label]['href']
    } else {
        currentHref = `${cardsParams[label]['href']}/${docId}`
    }

    return { ...item, ...{ icon : cardsParams[label]['icon'], href: currentHref } }
})
