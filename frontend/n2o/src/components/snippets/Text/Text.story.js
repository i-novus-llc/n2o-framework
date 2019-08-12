import React from 'react';
import { storiesOf } from '@storybook/react';
import Text from './Text';
import Factory from '../../../core/factory/Factory';
import { SNIPPETS } from '../../../core/factory/factoryLevels';

const stories = storiesOf('UI Компоненты/Текст', module);

stories
  .add('Компонент', () => <Text id="test" text="Some text..." />)
  .add('Создание через Factory', () => {
    const dt = {
      id: 'uniqId',
      src: 'Text',
      text: 'Text',
      format: null,
    };
    return (
      <React.Fragment>
        <Factory level={SNIPPETS} id={'uniqId'} {...dt} />
      </React.Fragment>
    );
  });
