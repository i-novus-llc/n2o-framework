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
import { withState } from '@dump247/storybook-state';
import _ from 'lodash';

import Alert from './Alert';
import Factory from '../../../core/factory/Factory';
import fetchMock from 'fetch-mock';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import widgetAlerts from 'N2oStorybook/json/widgetAlerts.json';
import globalAlert from 'N2oStorybook/json/globalAlerts.json';
import withPage from '../../../../.storybook/decorators/withPage';
import widgetWithErrors from '../../core/PageWithErrors';
import { GLOBAL_KEY } from '../../../constants/alerts';
import GlobalAlerts from '../../core/GlobalAlerts';
const stories = storiesOf('UI Компоненты/Сообщения', module);

stories.addDecorator(withKnobs);

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
  .add(
    'Компонент',
    withState({ visible: true })(({ store }) => {
      const knobs = {
        label: text('label', props.label),
        text: text('text', props.text),
        details: text('details', props.details),
        severity: select(
          'severity',
          ['info', 'danger', 'warning', 'success'],
          props.severity
        ),
        visible: boolean('visible', store.state.visible),
        closeButton: boolean('closeButton', props.closeButton),
      };

      return (
        <Alert
          {...knobs}
          onDismiss={e => {
            action('alert-onDismiss')(e);
            store.set({ visible: !store.state.visible });
          }}
        />
      );
    })
  )
  .add(
    'Без деталей',
    withState({ visible: true })(({ store }) => (
      <Alert
        {..._.omit(props, 'details')}
        visible={store.state.visible}
        onDismiss={e => {
          action('alert-onDismiss')(e);
          store.set({ visible: !store.state.visible });
        }}
      />
    ))
  )
  .add(
    'Без заголовка',
    withState({ visible: true })(({ store }) => (
      <Alert
        {..._.omit(props, 'label')}
        visible={store.state.visible}
        onDismiss={e => {
          action('alert-onDismiss')(e);
          store.set({ visible: !store.state.visible });
        }}
      />
    ))
  )
  .add(
    'Без кнопки скрытия',
    withState({ visible: true })(({ store }) => (
      <Alert
        {...props}
        closeButton={false}
        visible={store.state.visible}
        onDismiss={e => {
          action('alert-onDismiss')(e);
          store.set({ visible: !store.state.visible });
        }}
      />
    ))
  )
  .add(
    'Цвета',
    withState({ danger: true, warning: true, success: true, default: true })(
      ({ store }) => (
        <React.Fragment>
          <Alert
            {...props}
            visible={store.state.default}
            onDismiss={e => {
              action('alert-onDismiss')(e);
              store.set({ default: !store.state.default });
            }}
          />
          <Alert
            {...props}
            severity="danger"
            visible={store.state.danger}
            onDismiss={e => {
              action('alert-onDismiss')(e);
              store.set({ danger: !store.state.danger });
            }}
          />
          <Alert
            {...props}
            severity="warning"
            visible={store.state.warning}
            onDismiss={e => {
              action('alert-onDismiss')(e);
              store.set({ warning: !store.state.warning });
            }}
          />
          <Alert
            {...props}
            severity="success"
            visible={store.state.success}
            onDismiss={e => {
              action('alert-onDismiss')(e);
              store.set({ success: !store.state.success });
            }}
          />
        </React.Fragment>
      )
    )
  )
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
  });
