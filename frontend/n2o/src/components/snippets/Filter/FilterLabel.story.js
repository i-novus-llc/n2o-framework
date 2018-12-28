import React from 'react';
import { storiesOf } from '@storybook/react';
import { Form, FormGroup, Label, Input, Col } from 'reactstrap';

import Filter from './Filter';

const stories = storiesOf('UI Компоненты/Фильтр', module);

stories.add('Текст кнопок', () => {
  return (
    <Filter searchLabel="Свой текст поиска" resetLabel="Свой текст сброса">
      <Form>
        <FormGroup>
          <Label for="topInput">Почта</Label>
          <Input type="email" id="topInput" placeholder="Почта" />
        </FormGroup>
      </Form>
    </Filter>
  );
});
