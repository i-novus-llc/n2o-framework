import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, select } from '@storybook/addon-knobs/react';
import Text from './Text';
import Factory from '../../../core/factory/Factory';
import { SNIPPETS } from '../../../core/factory/factoryLevels';

const stories = storiesOf('UI Компоненты/Текст', module);

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);
stories
  .add('Компонент', () => <Text id="test" text="Some text..." />)
  .add('Создание через Factory', () => {
    const dt = {
      id: 'uniqId',
      src: 'Text',
      text: text('text', 'Text'),
      format: select(
        'format',
        ['date', 'password', 'number', 'dateFromNow'],
        null
      ),
    };
    return (
      <React.Fragment>
        <Factory level={SNIPPETS} id={'uniqId'} {...dt} />
      </React.Fragment>
    );
  });
