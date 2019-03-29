import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import Icon from './Icon';

const stories = storiesOf('UI Компоненты/Иконка', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Icon'));

stories.add('Компонент', () => {
  const props = {
    disabled: boolean('disabled', false),
    name: text('name', 'fa fa-user'),
    spin: boolean('spin', false),
    bordered: boolean('bordered', false),
    circular: boolean('circular', false)
  };

  return <Icon {...props} />;
});
