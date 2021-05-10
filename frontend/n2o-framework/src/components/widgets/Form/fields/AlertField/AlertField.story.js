import React from 'react'
import { storiesOf } from '@storybook/react'

import AlertField from './AlertField'

const stories = storiesOf('Виджеты/Форма/Fields/AlertField')

const props = {
    visible: true,
    className: '',
    text: 'Алерт!',
    color: 'info',
    fade: true,
}

stories.add('Компонент', () => <AlertField {...props} />, {
    info: {
        text: `
   Компонент AlertField
    ~~~js
    import AlertField from 'n2o-framework/lib/components/widgets/Form/fields/AlertField';
    
    <Alert
        header="Заголовок алерта"
        text="Текст алерта"
        footer="Футер алерта"
        visible={true}
        color="info"
        fade={true}
        tag="span"
     />
    ~~~
    `,
    },
})
