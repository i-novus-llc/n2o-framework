import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, number, select } from '@storybook/addon-knobs/react';
import OutputText from './OutputText';
import OutputJSON from './Output.meta';

const stories = storiesOf('Контролы/OutputText', module);

stories.addDecorator(withKnobs);

stories.add('Компонент', () => {
  const props = {
    type: select('type', ['iconAndText', 'icon', 'text'], OutputJSON.type),
    textPlace: select('textPlace', ['right', 'left'], OutputJSON.textPlace),
    icon: text('icon', OutputJSON.icon),
    value: text('value', 'text'),
    format: text('format', ''),
    disabled: boolean('disabled', OutputJSON.disabled)
  };

  return <OutputText {...props} />;
});
