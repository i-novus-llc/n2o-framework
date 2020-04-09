import React from 'react';
import { storiesOf } from '@storybook/react';

import SimpleButton from '../Simple/Simple';
import StandardButton from '../StandardButton/StandardButton';

const stories = storiesOf('Кнопки/SimpleButton', module);

const meta = {
  hint: 'Всплывающая подсказка',
  visible: true,
  rounded: true,
  disabled: false,
  icon: 'fa fa-plus',
  size: 'sm',
  color: 'primary',
  action: {
    type: 'n2o/button/Dummy',
  },
};

const meta1 = {
  hint: 'Всплывающая подсказка',
  visible: true,
  rounded: true,
  disabled: false,
  icon: 'fa fa-bolt',
  size: 'md',
  color: 'primary',
  action: {
    type: 'n2o/button/Dummy',
  },
};

const meta2 = {
  hint: 'Всплывающая подсказка',
  visible: true,
  rounded: true,
  disabled: false,
  icon: 'fa fa-plus',
  size: 'lg',
  color: 'primary',
  action: {
    type: 'n2o/button/Dummy',
  },
};

const performMeta = {
  hint: 'Всплывающая подсказка',
  confirm: {
    cancelLabel: 'Нет',
    okLabel: 'Да',
    text: "`'Выполнить действие над '+name+'?'`",
    title: 'Предупреждение',
  },
  visible: true,
  rounded: true,
  disabled: false,
  icon: 'fa fa-bolt',
  size: 'md',
  color: 'primary',
  action: {
    type: 'n2o/button/Dummy',
  },
};

stories
  .add('SimpleButton', () => (
    <div>
      <SimpleButton {...meta} />
      <SimpleButton {...meta} icon={'fa fa-minus'} />
      <SimpleButton {...meta1} />
      <SimpleButton {...meta1} icon={'fa fa-minus'} />
      <SimpleButton {...meta2} />
      <SimpleButton {...meta2} icon={'fa fa-minus'} />
    </div>
  ))
  .add('SimpleButton in StandardButton', () => (
    <div>
      <StandardButton {...performMeta} />
    </div>
  ));
