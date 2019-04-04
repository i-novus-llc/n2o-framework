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
  );
