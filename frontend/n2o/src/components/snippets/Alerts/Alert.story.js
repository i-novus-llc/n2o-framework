import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import {
  withKnobs,
  text,
  boolean,
  number,
  array,
  select,
} from '@storybook/addon-knobs/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withState } from '@dump247/storybook-state';
import _ from 'lodash';

import Alert from './Alert';
import Factory from '../../../core/factory/Factory';
import fetchMock from 'fetch-mock';
import { WIDGETS, SNIPPETS } from '../../../core/factory/factoryLevels';
import widgetAlerts from 'N2oStorybook/json/widgetAlerts.json';
import globalAlert from 'N2oStorybook/json/globalAlerts.json';
import withPage from '../../../../.storybook/decorators/withPage';
import widgetWithErrors from '../../core/PageWithErrors';
import { GLOBAL_KEY } from '../../../constants/alerts';
import GlobalAlerts from '../../core/GlobalAlerts';
const stories = storiesOf('UI Компоненты/Сообщения', module);

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);

const props = {
  label: 'Лейбл алерта',
  text: 'Текст алерта',
  details: 'Подробности алерта',
  severity: 'info',
  visible: true,
  closeButton: true,
};

stories
  .addDecorator(withPage(widgetWithErrors))
  .add('Компонент', () => {
    const knobs = {
      label: text('label', props.label),
      text: text('text', props.text),
      details: text('details', props.details),
      severity: select(
        'severity',
        ['info', 'danger', 'warning', 'success'],
        props.severity
      ),
      visible: boolean('visible', true),
      closeButton: boolean('closeButton', props.closeButton),
    };

    return (
      <Alert
        {...knobs}
        onDismiss={e => {
          action('alert-onDismiss')(e);
        }}
      />
    );
  })
  .add('Без деталей', () => (
    <Alert
      {..._.omit(props, 'details')}
      visible={true}
      onDismiss={e => {
        action('alert-onDismiss')(e);
      }}
    />
  ))
  .add('Без заголовка', () => (
    <Alert
      {..._.omit(props, 'label')}
      visible={true}
      onDismiss={e => {
        action('alert-onDismiss')(e);
      }}
    />
  ))
  .add('Без кнопки скрытия', () => (
    <Alert
      {...props}
      closeButton={false}
      visible={true}
      onDismiss={e => {
        action('alert-onDismiss')(e);
      }}
    />
  ))
  .add('Цвета', () => (
    <React.Fragment>
      <Alert
        {...props}
        visible={true}
        onDismiss={e => {
          action('alert-onDismiss')(e);
        }}
      />
      <Alert
        {...props}
        severity="danger"
        visible={true}
        onDismiss={e => {
          action('alert-onDismiss')(e);
        }}
      />
      <Alert
        {...props}
        severity="warning"
        visible={true}
        onDismiss={e => {
          action('alert-onDismiss')(e);
        }}
      />
      <Alert
        {...props}
        severity="success"
        visible={true}
        onDismiss={e => {
          action('alert-onDismiss')(e);
        }}
      />
    </React.Fragment>
  ))
  .add('Обновленный Alert', () => {
    const props = {
      label: 'Лейбл алерта',
      text: 'Текст алерта',
      details: 'Подробности алерта',
      severity: 'success',
      visible: true,
      closeButton: true,
      help: 'A little help',
    };

    return <Alert {...props} animate={true} />;
  })
  .add('С переносами текста', () => {
    const props = {
      label: 'Лейбл алерта',
      text: 'Текст алерта \n new line text',
      details: 'Подробности алерта',
      severity: 'success',
      visible: true,
      closeButton: true,
      help: 'A little help',
    };

    return <Alert {...props} animate={true} />;
  })
  .add('Alert loader', () => {
    return (
      <Alert {...props} loader={true} text={undefined} severity={'secondary'} />
    );
  })
  .add('Абсолютное и относительное позиционирование в виджете', () => {
    const widgetError = {
      meta: {
        alert: {
          alertKey: 'Page_Form',
          stacked: true,
          messages: [
            {
              severity: 'success',
              position: 'absolute',
              text: 'Алерт с анимацией и абсолютным позиционированием',
              animate: true,
            },
          ],
        },
      },
    };

    const widgetErrorStacked = {
      meta: {
        alert: {
          alertKey: 'Page_Form',
          stacked: true,
          messages: [
            {
              severity: 'danger',
              text: 'Алерт с анимацией и относительным позиционированием',
              animate: true,
            },
          ],
        },
      },
    };

    fetchMock
      .restore()
      .post('begin:n2o/data/test', {
        status: 200,
        body: widgetError,
      })
      .post('begin:n2o/data/stacked-test', {
        status: 200,
        body: widgetErrorStacked,
      });
    return (
      <React.Fragment>
        <Factory
          level={WIDGETS}
          id={'Page_Form'}
          {...widgetAlerts['Page_Form']}
        />
      </React.Fragment>
    );
  })
  .add('Global alert', () => {
    const widgetError = {
      meta: {
        alert: {
          alertKey: GLOBAL_KEY,
          stacked: true,
          messages: [
            {
              severity: 'success',
              position: 'absolute',
              text: 'Глобальный алерт',
              animate: true,
            },
          ],
        },
      },
    };

    fetchMock.restore().post('begin:n2o/data/test', {
      status: 200,
      body: widgetError,
    });
    return (
      <React.Fragment>
        <GlobalAlerts />
        <Factory
          level={WIDGETS}
          id={'Page_Form'}
          {...globalAlert['Page_Form']}
        />
      </React.Fragment>
    );
  })
  .add('Создание через Factory', () => {
    const dt = {
      id: 'widget',
      src: 'Alert',
      label: text('label', 'Лейбл'),
      text: text('text', 'Текст'),
      details: text('details', 'Детали'),
      severity: select(
        'severity',
        ['info', 'danger', 'warning', 'success'],
        'info'
      ),
      visible: boolean('visible', true),
      closeButton: boolean('closeButton', false),
    };
    return (
      <React.Fragment>
        <Factory level={SNIPPETS} id={'widget'} {...dt} />
      </React.Fragment>
    );
  });
