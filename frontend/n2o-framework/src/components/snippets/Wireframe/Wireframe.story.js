import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, select } from '@storybook/addon-knobs/react';
import { Card, CardBody } from 'reactstrap';

import Wireframe from './Wireframe';
import Factory from '../../../core/factory/Factory';
import { SNIPPETS } from '../../../core/factory/factoryLevels';

const stories = storiesOf('UI Компоненты/Визуальная заглушка', module);

stories.addDecorator(withKnobs);

stories
  .add('Компонент', () => {
    const props = {
      className: text('className', 'n2o'),
      title: text('title', 'Текст визуальной заглушки'),
    };

    return (
      <Card>
        <CardBody
          style={{
            padding: '60px',
            position: 'relative',
          }}
        >
          <Wireframe {...props} />
        </CardBody>
      </Card>
    );
  })
  .add('Создание через Factory', () => {
    const dt = {
      id: 'uniqId',
      src: 'Wireframe',
      className: text('className', 'n2o'),
      title: text('title', 'Текст визуальной заглушки'),
    };
    return (
      <React.Fragment>
        <Factory level={SNIPPETS} id={'uniqId'} {...dt} />
      </React.Fragment>
    );
  });
