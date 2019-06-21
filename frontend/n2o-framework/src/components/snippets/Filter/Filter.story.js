import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, object, array } from '@storybook/addon-knobs/react';
import { Form, FormGroup, Label, Input, Col } from 'reactstrap';
import { withState } from '@dump247/storybook-state';
import Filter from './Filter';

const stories = storiesOf('UI Компоненты/Фильтр', module);

stories.addDecorator(withKnobs);

stories.add(
  'Компонент',
  withState({ text: '' })(({ store }) => {
    const props = {
      style: object('style', {}),
      className: text('className', 'n2o'),
      filters: array('filters', []),
    };

    return (
      <Filter
        {...props}
        onReset={e => {
          store.set({ text: '' });
        }}
      >
        <Form>
          <FormGroup>
            <Label for="exampleEmail">Почта</Label>
            <Input
              type="email"
              id="exampleEmail"
              placeholder="Почта"
              value={store.state.text}
              onChange={e => {
                store.set({ text: e.target.value });
              }}
            />
          </FormGroup>
        </Form>
      </Filter>
    );
  })
);
