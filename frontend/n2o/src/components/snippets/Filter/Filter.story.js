import React from 'react';
import { storiesOf } from '@storybook/react';
import { Form, FormGroup, Label, Input } from 'reactstrap';
import Filter from './Filter';

const stories = storiesOf('UI Компоненты/Фильтр', module);

stories.addParameters({
  info: {
    propTablesExclude: [Form, FormGroup, Label, Input],
  },
});

stories
  .add('Компонент', () => {
    const props = {
      style: {},
      className: 'n2o',
      filters: [],
    };

    return (
      <Filter {...props} onReset={() => {}}>
        <Form>
          <FormGroup>
            <Label for="exampleEmail">Почта</Label>
            <Input
              type="email"
              id="exampleEmail"
              placeholder="Почта"
              value=""
              onChange={e => {}}
            />
          </FormGroup>
        </Form>
      </Filter>
    );
  })
  .add('Текст кнопок', () => {
    const knobs = {
      searchLabel: 'Свой текст поиска',
      resetLabel: 'Свой текст сброса',
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
