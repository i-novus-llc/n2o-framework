import React from 'react'
import { storiesOf } from '@storybook/react'

import Factory from '../../../core/factory/Factory'

import InputSelect, { N2OSelect } from './N2OSelect'
import N2OSelectJson from './N2OSelect.meta.json'

const stories = storiesOf('Контролы/Выпадающий список', module)

stories.addParameters({
    info: {
        propTables: [N2OSelect],
        propTablesExclude: [InputSelect, Factory],
    },
})

stories
    .add(
        'N2O вариант',
        () => {
            const options = [
                {
                    id: 'Алексей',
                    icon: 'fa fa-square',
                    image: 'https://i.stack.imgur.com/2zqqC.jpg',
                },
                {
                    id: 'Игорь',
                    icon: 'fa fa-plus',
                    image: 'https://i.stack.imgur.com/2zqqC.jpg',
                },
                {
                    id: 'Владимир',
                    icon: 'fa fa-square',
                    image: 'https://i.stack.imgur.com/2zqqC.jpg',
                },
                {
                    id: 'Анатолий',
                    icon: 'fa fa-square',
                    image: 'https://i.stack.imgur.com/2zqqC.jpg',
                },
                {
                    id: 'Николай',
                    icon: 'fa fa-plus',
                    image: 'https://i.stack.imgur.com/2zqqC.jpg',
                },
            ]

            const props = {
                loading: N2OSelectJson.loading,
                value: N2OSelectJson.value,
                disabled: N2OSelectJson.disabled,
                placeholder: N2OSelectJson.placeholder,
                hasSearch: N2OSelectJson.hasSearch,
                valueFieldId: N2OSelectJson.valueFieldId,
                labelFieldId: N2OSelectJson.labelFieldId,
                filter: N2OSelectJson.filter,
                resetOnBlur: N2OSelectJson.resetOnBlur,
                iconFieldId: N2OSelectJson.iconFieldId,
                imageFieldId: N2OSelectJson.imageFieldId,
                groupFieldId: N2OSelectJson.groupFieldId,
                hasCheckboxes: N2OSelectJson.hasCheckboxes,
                closePopupOnSelect: N2OSelectJson.closePopupOnSelect,
                cleanable: N2OSelectJson.cleanable,
                format: N2OSelectJson.format,
            }

            return <InputSelect {...props} options={options} />
        },
        {
            info: {
                text: `
    Компонент 'Выпадающий список N2O'
    ~~~js
    import N2OSelect from 'n2o-framework/lib/components/controls/N2OSelect/N2OSelect';
    
    const options = [
        {
          id: 'Алексей',
          icon: 'fa fa-square',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Игорь',
          icon: 'fa fa-plus',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Владимир',
          icon: 'fa fa-square',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Анатолий',
          icon: 'fa fa-square',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Николай',
          icon: 'fa fa-plus',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
     ];
     
    <N2OSelect 
        placeholder="Введите"
        valueFieldId="id"
        labelFieldId="id"
        iconFieldId="icon"
        imageFieldId="image"
        groupFieldId="icon"
        filter="includes"
        hasCheckboxes={true}
        closePopupOnSelect={true}
        format="\`id + ' ' + id\`"
        hasSearch={true}
        cleanable={true}
    />
    ~~~
    `,
            },
        },
    )
    .add(
        'Элементы с дополнительным описанием',
        () => {
            const options = [
                {
                    id: 'Алексей',
                    icon: 'fa fa-square',
                    image: 'https://i.stack.imgur.com/2zqqC.jpg',
                    desc: 'описание',
                },
                {
                    id: 'Игорь',
                    icon: 'fa fa-plus',
                    image: 'https://i.stack.imgur.com/2zqqC.jpg',
                    desc: 'описание',
                },
                {
                    id: 'Владимир',
                    icon: 'fa fa-square',
                    image: 'https://i.stack.imgur.com/2zqqC.jpg',
                },
                {
                    id: 'Анатолий',
                    icon: 'fa fa-square',
                    image: 'https://i.stack.imgur.com/2zqqC.jpg',
                },
                {
                    id: 'Николай',
                    icon: 'fa fa-plus',
                    image: 'https://i.stack.imgur.com/2zqqC.jpg',
                },
            ]

            const props = {
                loading: N2OSelectJson.loading,
                value: N2OSelectJson.value,
                disabled: N2OSelectJson.disabled,
                placeholder: N2OSelectJson.placeholder,
                hasSearch: N2OSelectJson.hasSearch,
                valueFieldId: N2OSelectJson.valueFieldId,
                labelFieldId: N2OSelectJson.labelFieldId,
                filter: N2OSelectJson.filter,
                resetOnBlur: N2OSelectJson.resetOnBlur,
                iconFieldId: N2OSelectJson.iconFieldId,
                imageFieldId: N2OSelectJson.imageFieldId,
                groupFieldId: N2OSelectJson.groupFieldId,
                hasCheckboxes: N2OSelectJson.hasCheckboxes,
                closePopupOnSelect: N2OSelectJson.closePopupOnSelect,
                cleanable: N2OSelectJson.cleanable,
                format: N2OSelectJson.format,
            }

            return (
                <N2OSelect {...props} options={options} descriptionFieldId="desc" />
            )
        },
        {
            info: {
                text: `
    Компонент 'Выпадающий список N2O'
    ~~~js
    import N2OSelect from 'n2o-framework/lib/components/controls/N2OSelect/N2OSelect';
    
    const options = [
        {
          id: 'Алексей',
          icon: 'fa fa-square',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Игорь',
          icon: 'fa fa-plus',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Владимир',
          icon: 'fa fa-square',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Анатолий',
          icon: 'fa fa-square',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
          id: 'Николай',
          icon: 'fa fa-plus',
          image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
     ];
     
    <N2OSelect 
        placeholder="Введите"
        valueFieldId="id"
        labelFieldId="id"
        iconFieldId="icon"
        imageFieldId="image"
        groupFieldId="icon"
        filter="includes"
        hasCheckboxes={true}
        closePopupOnSelect={true}
        format="\`id + ' ' + id\`"
        hasSearch={true}
        cleanable={true}
    />
    ~~~
    `,
            },
        },
    )
