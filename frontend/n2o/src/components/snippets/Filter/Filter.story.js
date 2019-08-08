import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, object, array } from '@storybook/addon-knobs/react';
import { Form, FormGroup, Label, Input } from 'reactstrap';
import { action } from '@storybook/addon-actions';
import Filter from './Filter';

const stories = storiesOf('UI Компоненты/Фильтр', module);

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);
stories.addParameters({
  info: {
    propTablesExclude: [Form, FormGroup, Label, Input],
  },
});

stories
  .add('Компонент', () => {
    const props = {
      style: object('style', {}),
      className: text('className', 'n2o'),
      filters: array('filters', []),
    };

    return (
      <Filter {...props} onReset={e => action('filter-onReset')(e)}>
        <Form>
          <FormGroup>
            <Label for="exampleEmail">Почта</Label>
            <Input
              type="email"
              id="exampleEmail"
              placeholder="Почта"
              value=""
              onChange={e => {
                action('filter-onChange')(e);
              }}
            />
          </FormGroup>
        </Form>
      </Filter>
    );
  })
  .add('Текст кнопок', () => {
    const knobs = {
      searchLabel: text('searchLabel', 'Свой текст поиска'),
      resetLabel: text('resetLabel', 'Свой текст сброса'),
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
