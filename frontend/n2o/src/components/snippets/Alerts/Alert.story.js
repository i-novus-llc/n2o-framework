import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import { withKnobs, text, boolean, number, array, select } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import _ from 'lodash';

import Alert from './Alert';

const stories = storiesOf('UI Компоненты/Сообщения', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Alert'));

const props = {
  label: 'Лейбл алерта',
  text: 'Текст алерта',
  details: 'Подробности алерта',
  severity: 'info',
  visible: true,
  closeButton: true
};

stories
  .add('Компонент', () => {
    const knobs = {
      label: text('label', props.label),
      text: text('text', props.text),
      details: text('details', props.details),
      severity: select('severity', ['info', 'danger', 'warning', 'success'], props.severity),
      visible: boolean('visible', props.visible),
      closeButton: boolean('closeButton', props.closeButton)
    };

    return <Alert {...knobs} onDismiss={action('alert-onDismiss')} />;
  })
  .add('Без деталей', () => {
    return <Alert {..._.omit(props, 'details')} onDismiss={action('alert-onDismiss')} />;
  })
  .add('Без заголовка', () => {
    return <Alert {..._.omit(props, 'label')} onDismiss={action('alert-onDismiss')} />;
  })
  .add('Без кнопки скрытия', () => {
    return <Alert {...props} closeButton={false} onDismiss={action('alert-onDismiss')} />;
  })
  .add('Цвета', () => {
    return (
      <React.Fragment>
        <Alert {...props} onDismiss={action('alert-onDismiss')} />
        <Alert {...props} severity="danger" onDismiss={action('alert-onDismiss')} />
        <Alert {...props} severity="warning" onDismiss={action('alert-onDismiss')} />
        <Alert {...props} severity="success" onDismiss={action('alert-onDismiss')} />
      </React.Fragment>
    );
  })
  .add('Динамическое использование', () => {
    class AlertContainer extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          visible: true
        };
      }
      render() {
        return (
          <Alert
            {...props}
            visible={this.state.visible}
            onDismiss={() => this.setState({ visible: false })}
          />
        );
      }
    }
    return <AlertContainer />;
  });
