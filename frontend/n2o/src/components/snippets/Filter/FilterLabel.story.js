import React from 'react';
import { storiesOf } from '@storybook/react';
import { Form, FormGroup, Label, Input } from 'reactstrap';
import { withKnobs, text, boolean, number, array, select } from '@storybook/addon-knobs/react';

import Filter from './Filter';

const stories = storiesOf('UI Компоненты/Фильтр', module);
stories.addDecorator(withKnobs);
stories.add('Текст кнопок', () => {
  const knobs = {
    searchLabel: text('searchLabel', 'Свой текст поиска'),
    resetLabel: text('resetLabel', 'Свой текст сброса')
  };

  return (
    <Filter {...knobs}>
      <Form>
        <FormGroup>
          <Label for="topInput">Почта</Label>
          <Input type="email" id="topInput" placeholder="Почта" />
        </FormGroup>
      </Form>
    </Filter>
  );
});
