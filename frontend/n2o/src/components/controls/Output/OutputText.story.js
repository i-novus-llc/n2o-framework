import React from 'react';
import { storiesOf } from '@storybook/react';

import OutputText from './OutputText';
import OutputJSON from './Output.meta';

const stories = storiesOf('Контролы/OutputText', module);

const props = {
  type: OutputJSON.type,
  textPlace: OutputJSON.textPlace,
  icon: OutputJSON.icon,
  value: 'text',
  format: '',
  disabled: OutputJSON.disabled,
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
