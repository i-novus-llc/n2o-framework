import React from 'react';
import { storiesOf } from '@storybook/react';
import {
  withKnobs,
  text,
  boolean,
  number,
  select,
} from '@storybook/addon-knobs/react';
import OutputText from './OutputText';
import OutputJSON from './Output.meta';

const stories = storiesOf('Контролы/OutputText', module);

stories.addDecorator(withKnobs);

const props = {
  type: select('type', ['iconAndText', 'icon', 'text'], OutputJSON.type),
  textPlace: select('textPlace', ['right', 'left'], OutputJSON.textPlace),
  icon: text('icon', OutputJSON.icon),
  value: text('value', 'text'),
  format: select(
    'format',
    ['', 'dateFromNow', 'password', 'number 0,0.00'],
    ''
  ),
  disabled: boolean('disabled', OutputJSON.disabled),
};

const component = (propsOverride = {}, style = { width: 200 }) => (
  <div style={style}>
    <OutputText {...props} {...propsOverride} />
  </div>
);

stories
  .add('Компонент', () => {
    return component();
  })
  .add('Длинная строка с переносом', () => {
    return component({
      value:
        'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente.',
    });
  })
  .add('Длинная строка с ellipsis', () => {
    return component({
      value:
        'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente.',
      ellipsis: true,
    });
  })
  .add('"Подробнее" в конце строки', () => {
    return component(
      {
        value:
          'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente.',
        expandable: true,
      },
      { width: 400 }
    );
  })
  .add('"Подробнее" по количеству символов', () => {
    return component({
      value:
        'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente.',
      expandable: 40,
    });
  });
