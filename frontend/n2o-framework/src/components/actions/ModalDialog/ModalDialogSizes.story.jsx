import React from 'react'
import { storiesOf, forceReRender } from '@storybook/react'
import { StateDecorator, Store } from '@sambego/storybook-state'

import ModalDialog from './ModalDialog'

const store = new Store({
    visible: false,
    size: 'sm',
})

store.subscribe(forceReRender)

const stories = storiesOf('UI Компоненты/Диалог', module)

stories.addDecorator(StateDecorator(store))

stories.add(
    'Размеры',
    () => [
        <div className="btn-group">
            <button
                className="btn btn-secondary"
                onClick={() => store.set({
                    visible: true,
                    size: 'sm',
                })
                }
            >
        Показать маленький
            </button>
            <button
                className="btn btn-secondary"
                onClick={() => store.set({
                    visible: true,
                    size: 'lg',
                })
                }
            >
        Показать большой
            </button>
        </div>,
        <ModalDialog
            size={store.get('size')}
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
            {...props}
            size="sm" 
        />
        <ModalDialog
            {...props}
            size="lg" 
        />
        ~~~
        `,
        },
    },
)
