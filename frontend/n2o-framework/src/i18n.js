import i18n from 'i18next'
import { initReactI18next } from 'react-i18next'
import intervalPlural from 'i18next-intervalplural-postprocessor'

import resources from './locales'

i18n
    .use(initReactI18next) // passes i18n down to react-i18next
    .use(intervalPlural)
    .init({
        resources,
        lng: 'ru',

        keySeparator: false, // we do not use keys in form messages.welcome

        interpolation: {
            escapeValue: false, // react already safes from xss
        },
    })

export default i18n
