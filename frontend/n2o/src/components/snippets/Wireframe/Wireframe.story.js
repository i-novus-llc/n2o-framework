import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text } from '@storybook/addon-knobs/react';
import { Card, CardBody } from 'reactstrap';

import Wireframe from './Wireframe';

const stories = storiesOf('UI Компоненты/Визуальная заглушка', module);

stories.addDecorator(withKnobs);

stories.add('Компонент', () => {
  const props = {
    className: text('className', 'n2o'),
    title: text('title', 'Текст визуальной заглушки')
  };

  return (
    <Card>
      <CardBody
        style={{
          padding: '60px',
          position: 'relative'
        }}
      >
        <Wireframe {...props} />
      </CardBody>
    </Card>
  );
});
