import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, object, array } from '@storybook/addon-knobs/react';
import { Form, FormGroup, Label, Input, Col } from 'reactstrap';

import Filter from './Filter';

const stories = storiesOf('UI Компоненты/Фильтр', module);

stories.addDecorator(withKnobs);

stories.add('Компонент', () => {
  const props = {
    style: object('style', {}),
    className: text('className', 'n2o'),
    filters: array('filters', [])
  };

  return (
    <Filter {...props}>
      <Form>
        <FormGroup>
          <Label for="exampleEmail">Почта</Label>
          <Input type="email" id="exampleEmail" placeholder="Почта" />
        </FormGroup>
      </Form>
    </Filter>
  );
});
