import React from 'react'
import { storiesOf } from '@storybook/react'
import Button from 'reactstrap/lib/Button'

import PopoverConfirm from '../../snippets/PopoverConfirm/PopoverConfirm'

import MetaJson from './PopoverConfirm.meta'

const stories = storiesOf('Действия/PopoverConfirm', module)

const props = {
    title: MetaJson.title,
    text: MetaJson.text,
    okLabel: MetaJson.okLabel,
    cancelLabel: MetaJson.cancelLabel,
}

stories.add(
    'Компонент',
    () => (
        <div className="col-md-12 d-flex justify-content-center">
            <PopoverConfirm {...props}>
                <Button>Показать confirm</Button>
            </PopoverConfirm>
        </div>
    ),
    {
        jsx: { skip: 1 },
        info: {
            text: `
        Компонент 'PopoverConfirm'

        ~~~js
        import PopoverConfirm from 'n2o-framework/lib/components/actions/PopoverConfirm/PopoverConfirm';
        
        <PopoverConfirm 
            title="Вы уверены?"
            text="Вы хотите совершить действие?"
            okLabel="Ок"
            cancelLabel="Отмена"
            onDeny={onDeny}
            onConfirm={onConfirm}
        />
        ~~~
        `,
        },
    },
)
