import React from 'react';
import { storiesOf } from '@storybook/react';
import { withState } from '@dump247/storybook-state';
import { Form, FormGroup, Label, Input, Col, Button } from 'reactstrap';

import ModalDialog from './ModalDialog';

const stories = storiesOf('UI Компоненты/Диалог', module);

stories.add(
  'Текст',
  withState(
    {
      visible: false,
      title: 'Подтвердите действие',
      text: 'Вы уверены?',
      confirmText: 'Да',
      denyText: 'Нет',
    },
    store => {
      return (
        <React.Fragment>
          <Form>
            <FormGroup>
              <Label for="Title">Заголовок диалога</Label>
              <Input
                type="text"
                placeholder="Заголовок диалога"
                value={store.state.title}
                onChange={e => store.set({ title: e.target.value })}
                id="Title"
              />
            </FormGroup>
            <FormGroup>
              <Label for="Text">Текст диалога</Label>
              <Input
                id="Text"
                type="text"
                placeholder="Текст диалога"
                value={store.state.text}
                onChange={e => store.set({ text: e.target.value })}
              />
            </FormGroup>
            <FormGroup>
              <Label for="Confirm">Текст подтверждения</Label>
              <Input
                id="Confirm"
                type="text"
                placeholder="Текст подтверждения"
                value={store.state.confirmText}
                onChange={e => store.set({ confirmText: e.target.value })}
              />
            </FormGroup>
            <FormGroup>
              <Label for="Deny">Текст отказа</Label>
              <Input
                type="text"
                placeholder="Текст отказа"
                value={store.state.denyText}
                onChange={e => store.set({ denyText: e.target.value })}
              />
            </FormGroup>
            <FormGroup>
              <Button onClick={() => store.set({ visible: true })}>
                Показать диалог
              </Button>
            </FormGroup>
          </Form>

          <ModalDialog
            title={store.state.title}
            text={store.state.text}
            confirmText={store.state.confirmText}
            denyText={store.state.denyText}
            visible={store.state.visible}
            onDeny={() => store.set({ visible: false })}
            onConfirm={() => store.set({ visible: false })}
          />
        </React.Fragment>
      );
    }
  )
);
