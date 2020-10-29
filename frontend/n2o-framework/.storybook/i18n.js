import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import intervalPlural from 'i18next-intervalplural-postprocessor';

import resources from '../src/locales/index'

export default i18n
  .use(initReactI18next)
  .use(intervalPlural)
  .init({
    lng: 'ru',
    interpolation: {
      escapeValue: false,
    },
    resources,
});
