import React from 'react';
import { storiesOf } from '@storybook/react';
import fetchMock from 'fetch-mock';
import widgetWithErrors from './PageWithErrors';
import withPage from '../../../.storybook/decorators/withPage';
import Factory from '../../core/factory/Factory';
import { WIDGETS } from '../../core/factory/factoryLevels';

const stories = storiesOf('Функциональность', module);

stories.addDecorator(withPage(widgetWithErrors)).add('Обработка ошибок', () => {
  const widgetError = {
    severity: 'danger',
    text: 'Произошла ошибка при получении данных'
  };
  const selectError = {
    severity: 'danger',
    text: 'Произошла ошибка при получении данных'
  };
  const invokeError = {
    severity: 'danger',
    text: 'Произошла ошибка при сохранении данных'
  };

  fetchMock
    .restore()
    .post('begin:n2o/data', {
      status: 500,
      body: JSON.stringify(invokeError)
    })
    .get('begin:n2o/data', {
      status: 404,
      body: JSON.stringify(widgetError)
    })
    .get('begin:n2o/inputSelect', {
      status: 404,
      body: JSON.stringify(selectError)
    });
  return <Factory level={WIDGETS} id={'Page_Form'} {...widgetWithErrors['Page_Form']} />;
});
