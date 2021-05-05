import React from 'react'
import { storiesOf, forceReRender } from '@storybook/react'
import { StateDecorator, Store } from '@sambego/storybook-state'

import ModalDialog from './ModalDialog'

const store = new Store({
    visible: false,
    closeButton: false,
})

store.subscribe(forceReRender)

const stories = storiesOf('UI Компоненты/Диалог', module)

stories.addDecorator(StateDecorator(store))

stories.add(
    'Кнопка закрытия',
    () => [
        <div className="btn-group">
            <button
                className="btn btn-secondary"
                onClick={() => store.set({
                    visible: true,
                    closeButton: true,
                })
                }
            >
        С кнопкой закрытия
            </button>
            <button
                className="btn btn-secondary"
                onClick={() => store.set({
                    visible: true,
                    closeButton: false,
                })
                }
            >
        Без кнопки закрытия
            </button>
        </div>,
        <ModalDialog
            closeButton={store.get('closeButton')}
            visible={store.get('visible')}
            onDeny={() => store.set({ visible: false })}
            onConfirm={() => store.set({ visible: false })}
            close={() => store.set({ visible: false })}
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
            closeButton={true} 
        />
        <ModalDialog
            {...props}
            closeButton={false} 
        />
        ~~~
        `,
        },
    },
)
