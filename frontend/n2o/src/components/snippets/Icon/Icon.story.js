import React from 'react';
import { storiesOf } from '@storybook/react';

import withTests from 'N2oStorybook/withTests';
import Factory from '../../../core/factory/Factory';
import { SNIPPETS } from '../../../core/factory/factoryLevels';
import Icon from './Icon';

const stories = storiesOf('UI Компоненты/Иконка', module);

stories.addDecorator(withTests('Icon'));

stories
  .add('Компонент', () => {
    const props = {
      disabled: false,
      name: 'fa fa-user',
      spin: false,
      bordered: false,
      circular: false,
    };

    return <Icon {...props} />;
  })
  .add('Создание через Factory', () => {
    const dt = {
      id: 'uniqId',
      src: 'Icon',
      disabled: false,
      name: 'fa fa-user',
      spin: false,
      bordered: false,
      circular: false,
    };
    return (
      <React.Fragment>
        <Factory level={SNIPPETS} id={'uniqId'} {...dt} />
      </React.Fragment>
    );
  });
