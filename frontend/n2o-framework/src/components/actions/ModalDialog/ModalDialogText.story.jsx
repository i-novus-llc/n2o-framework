import React from 'react'
import { storiesOf, forceReRender } from '@storybook/react'
import { StateDecorator, Store } from '@sambego/storybook-state'
import Form from 'reactstrap/lib/Form'
import FormGroup from 'reactstrap/lib/FormGroup'
import Label from 'reactstrap/lib/Label'
import Input from 'reactstrap/lib/Input'
import Button from 'reactstrap/lib/Button'

import ModalDialog from './ModalDialog'

const store = new Store({
    visible: false,
    title: 'Подтвердите действие',
    text: 'Вы уверены?',
    okLabel: 'Да',
    cancelLabel: 'Нет',
})

store.subscribe(forceReRender)

const stories = storiesOf('UI Компоненты/Диалог', module)
stories.addDecorator(StateDecorator(store))
stories.addParameters({
    info: {
        propTablesExclude: [Form, FormGroup, Label, Input, Button],
    },
})

stories.add(
    'Текст',
    () => [
        <Form>
            <FormGroup>
                <Label for="Title">Заголовок диалога</Label>
                <Input
                    type="text"
                    placeholder="Заголовок диалога"
                    value={store.get('title')}
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
                    value={store.get('text')}
                    onChange={e => store.set({ text: e.target.value })}
                />
            </FormGroup>
            <FormGroup>
                <Label for="Confirm">Текст подтверждения</Label>
                <Input
                    id="Confirm"
                    type="text"
                    placeholder="Текст подтверждения"
                    value={store.get('okLabel')}
                    onChange={e => store.set({ okLabel: e.target.value })}
                />
            </FormGroup>
            <FormGroup>
                <Label for="Deny">Текст отказа</Label>
                <Input
                    type="text"
                    placeholder="Текст отказа"
                    value={store.get('cancelLabel')}
                    onChange={e => store.set({ cancelLabel: e.target.value })}
                />
            </FormGroup>
            <FormGroup>
                <Button onClick={() => store.set({ visible: true })}>
          Показать диалог
                </Button>
            </FormGroup>
        </Form>,
        <ModalDialog
            title={store.get('title')}
            text={store.get('text')}
            okLabel={store.get('okLabel')}
            cancelLabel={store.get('cancelLabel')}
            visible={store.get('visible')}
            onDeny={() => store.set({ visible: false })}
            onConfirm={() => store.set({ visible: false })}
        />,
    ],
    {
        info: {
            text: `
        Компонент 'Модальное окно подтверждения'

        ~~~js
        import ModalDialog from 'n2o-framework/lib/components/actions/ModalDialog/ModalDialog';
        
        <ModalDialog
            title={title}
            text={text}
             okLabel={okLabel}
             cancelLabel={cancelLabel}
             visible={visible}
             onDeny={onDeny}
             onConfirm={onConfirm}
        />
        ~~~
        `,
        },
    },
)
