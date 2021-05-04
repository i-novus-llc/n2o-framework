import React from 'react'
import { storiesOf } from '@storybook/react'
import fetchMock from 'fetch-mock'
import { parseUrl, getStubData } from 'N2oStorybook/fetchMock'
import withForm from 'N2oStorybook/decorators/withForm'
import { excludeMetadata } from 'N2oStorybook/json'

import Factory from '../../../core/factory/Factory'

import N2OSelectContainer from './N2OSelectContainer'
import { N2OSelect } from './N2OSelect'
import N2OSelectContainerJson from './N2OSelectContainer.meta.json'

const form = withForm({ src: 'N2OSelect' })
const stories = storiesOf('Контролы/Выпадающий список', module)

stories.addParameters({
    info: {
        propTables: [N2OSelect],
        propTablesExclude: [Factory, N2OSelectContainer],
    },
})

const dataUrl = 'begin:n2o/data/test'

const data = [
    {
        id: 'Алексей Николаев',
        icon: 'fa fa-square',
        image: 'https://i.stack.imgur.com/2zqqC.jpg',
    },
    {
        id: 'Игорь Николаев',
        icon: 'fa fa-plus',
        image: 'https://i.stack.imgur.com/2zqqC.jpg',
    },
    {
        id: 'Владимир Серпухов',
        icon: 'fa fa-square',
        image: 'https://i.stack.imgur.com/2zqqC.jpg',
    },
    {
        id: 'Анатолий Петухов',
        icon: 'fa fa-square',
        image: 'https://i.stack.imgur.com/2zqqC.jpg',
    },
    {
        id: 'Анатолий Петухов',
        icon: 'fa fa-plus',
        image: 'https://i.stack.imgur.com/2zqqC.jpg',
    },
]

const handleData = data => (url) => {
    const mergeData = { ...N2OSelectContainerJson, list: data }
    const filterValue = parseUrl(decodeURI(url))

    if (filterValue.search) {
        const newData = data.filter(item => item[N2OSelectContainerJson.labelFieldId].includes(filterValue.search))

        return { ...N2OSelectContainerJson, list: newData }
    }

    return mergeData
}

const jsonWithoutOptions = excludeMetadata(N2OSelectContainerJson, ['options'])

stories
    .add(
        'Контейнер для N2O',
        form(() => {
            const props = {
                loading: N2OSelectContainerJson.loading,
                value: N2OSelectContainerJson.value,
                disabled: N2OSelectContainerJson.disabled,
                placeholder: N2OSelectContainerJson.placeholder,
                valueFieldId: N2OSelectContainerJson.valueFieldId,
                labelFieldId: N2OSelectContainerJson.labelFieldId,
                filter: N2OSelectContainerJson.filter,
                resetOnBlur: N2OSelectContainerJson.resetOnBlur,
                queryId: N2OSelectContainerJson.queryId,
                size: N2OSelectContainerJson.size,
                iconFieldId: N2OSelectContainerJson.iconFieldId,
                imageFieldId: N2OSelectContainerJson.imageFieldId,
                groupFieldId: N2OSelectContainerJson.groupFieldId,
                hasCheckboxes: N2OSelectContainerJson.hasCheckboxes,
                cleanable: N2OSelectContainerJson.cleanable,
                closePopupOnSelect: N2OSelectContainerJson.closePopupOnSelect,
                format: N2OSelectContainerJson.format,
                searchByTap: N2OSelectContainerJson.searchByTap,
                dataProvider: N2OSelectContainerJson.dataProvider,
            }

            fetchMock.restore().get(dataUrl, handleData(data))

            return props
        }),
    )

    .add('Список из метаданных', () => <N2OSelectContainer {...N2OSelectContainerJson} />)

    .add(
        'Кеширование запросов',
        () => {
            fetchMock.restore().get(dataUrl, handleData(data))

            return (
                <>
                    <div className="row">
                        <N2OSelectContainer
                            {...jsonWithoutOptions}
                            placeholder="Стандартный"
                        />
                    </div>
                    <div className="row" style={{ marginTop: '10px' }}>
                        <N2OSelectContainer
                            {...jsonWithoutOptions}
                            placeholder="С кешированием"
                            caching
                        />
                    </div>
                </>
            )
        },
        {
            info: {
                text: `
    Компонент 'Выпадающий список N2O'
    ~~~js
    import N2OSelectContainer from 'n2o-framework/lib/components/controls/N2OSelect/N2OSelectContainer';
     
    <N2OSelectContainer 
        {...props}
        placeholder="С кешированием"
        caching={true}
    />
    ~~~
    `,
            },
        },
    )

    .add(
        'Расширяемый popUp',
        () => {
            const newProps = {
                filter: 'includes',
                iconFieldId: '',
                imageFieldId: '',
                groupFieldId: '',
                format: '',
            }
            const props = { ...jsonWithoutOptions, ...newProps }
            const data = [
                {
                    id: 'Крузенштерн Розенкранц Николаевич',
                },
                {
                    id: 'Комаровский Иммануил Васильевич',
                },
                {
                    id: 'Петровский Сергей Вахтангович',
                },
            ]

            fetchMock.restore().get(dataUrl, handleData(data))

            return (
                <div style={{ width: '200px' }}>
                    <N2OSelectContainer {...props} />
                </div>
            )
        },
        {
            info: {
                text: `
    Компонент 'Выпадающий список N2O'
    ~~~js
    import N2OSelectContainer from 'n2o-framework/lib/components/controls/N2OSelect/N2OSelectContainer';
     
   const data = [
      {
        id: 'Крузенштерн Розенкранц Николаевич',
      },
      {
        id: 'Комаровский Иммануил Васильевич',
      },
      {
        id: 'Петровский Сергей Вахтангович',
      },
    ];
     
    <N2OSelectContainer 
        {...props}
    />
    ~~~
    `,
            },
        },
    )

    .add(
        'Баджи',
        () => {
            const newProps = {
                filter: 'includes',
                iconFieldId: '',
                imageFieldId: '',
                badgeFieldId: 'badge',
                groupFieldId: '',
                format: '',
                badgeColorFieldId: 'color',
            }
            const props = { ...jsonWithoutOptions, ...newProps }
            const data = [
                {
                    id: 'Розенкранц',
                    badge: 'Писатель',
                },
                {
                    id: 'Иммануил',
                    badge: 'Художник',
                    color: 'danger',
                },
                {
                    id: 'Сергей',
                    badge: 'Поэт',
                    color: 'info',
                },
            ]

            fetchMock.restore().get(dataUrl, handleData(data))

            return <N2OSelectContainer {...props} />
        },
        {
            info: {
                text: `
    Компонент 'Выпадающий список N2O'
    ~~~js
    import N2OSelectContainer from 'n2o-framework/lib/components/controls/N2OSelect/N2OSelectContainer';
     
   const data = [
      {
        id: 'Розенкранц',
        badge: 'Писатель',
      },
      {
        id: 'Иммануил',
        badge: 'Художник',
        color: 'danger',
      },
      {
        id: 'Сергей',
        badge: 'Поэт',
        color: 'info',
      },
    ];
     
    <N2OSelectContainer 
        {...props}
        filter="includes"
        badgeFieldId="badge"
        badgeColorFieldId="color"
    />
    ~~~
    `,
            },
        },
    )

    .add('Сжатие текста', () => {
        const newProps = {
            filter: 'includes',
            iconFieldId: '',
            imageFieldId: '',
            groupFieldId: '',
            format: '',
        }
        const props = { ...jsonWithoutOptions, ...newProps }
        const data = [
            {
                id:
          '«Русла́н и Людми́ла» — первая законченная поэма Александра Сергеевича Пушкина; волшебная сказка, вдохновлённая древнерусскими былинами.',
            },
            {
                id:
          'Поэма написана в 1818—1820, после выхода из Лицея; Пушкин иногда указывал, что начал писать поэму ещё в Лицее, но, по-видимому, к этому времени относятся лишь самые общие замыслы, едва ли текст. Ведя после выхода из Лицея в Петербурге жизнь «самую рассеянную», Пушкин работал над поэмой в основном во время болезней.',
            },
            {
                id:
          'Пушкин ставил задачей создать «богатырскую» сказочную поэму в духе известного ему по французским переводам «Неистового Роланда» Ариосто (критики называли этот жанр «романтическим», что не следует путать с романтизмом в современном понимании).',
            },
        ]

        fetchMock.restore().get(dataUrl, handleData(data))

        return <N2OSelectContainer {...props} />
    })

    .add('Отображение выбранных элементов в виде счетчика', () => {
        const newProps = {
            format: '',
            type: 'checkboxes',
            closePopupOnSelect: false,
            selectFormatOne: '{size} машина',
            selectFormatFew: '{size} машины',
            selectFormatMany: '{size} машин',
        }
        const props = { ...jsonWithoutOptions, ...newProps }
        const data = [
            {
                id: 'Lada',
            },
            {
                id: 'Volvo',
            },
            {
                id: 'Chevrolet',
            },
        ]

        fetchMock.restore().get(dataUrl, handleData(data))

        return <N2OSelectContainer {...props} />
    })
