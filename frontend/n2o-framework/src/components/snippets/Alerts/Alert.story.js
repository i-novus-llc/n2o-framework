import React from 'react'
import { storiesOf } from '@storybook/react'
import omit from 'lodash/omit'
import fetchMock from 'fetch-mock'
import widgetAlerts from 'N2oStorybook/json/widgetAlerts.json'
import globalAlert from 'N2oStorybook/json/globalAlerts.json'

import { WIDGETS, SNIPPETS } from '../../../core/factory/factoryLevels'
import Factory from '../../../core/factory/Factory'
import withPage from '../../../../.storybook/decorators/withPage'
import widgetWithErrors from '../../core/PageWithErrors'
import { GLOBAL_KEY } from '../../../constants/alerts'
import GlobalAlerts from '../../core/GlobalAlerts'

import Alert from './Alert'

const stories = storiesOf('UI Компоненты/Сообщения', module)

const props = {
    label: 'Лейбл алерта',
    text: 'Текст алерта',
    details: 'Подробности алерта',
    severity: 'info',
    visible: true,
    closeButton: true,
}

stories.addParameters({
    info: {
        propTables: [Alert],
        propTablesExclude: [Factory],
    },
})

stories
    .addDecorator(withPage(widgetWithErrors))
    .add(
        'Компонент',
        () => {
            const knobs = {
                label: props.label,
                text: props.text,
                details: props.details,
                severity: props.severity,
                visible: true,
                closeButton: props.closeButton,
            }

            return <Alert {...knobs} onDismiss={(e) => {}} />
        },
        {
            info: {
                text: `
      ~~~js
      import Alert from 'n2o-framework/lib/components/snippets/Alert/Alert';
      
      <Alert
          label="Лейбл алерта"
          text="Текст алерта"
          visible={true}
          severity="info"
          details="Подробности алерта"
          closeButton={true}
          onDismiss={onDismiss}
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Без деталей',
        () => (
            <Alert {...omit(props, 'details')} visible onDismiss={(e) => {}} />
        ),
        {
            info: {
                text: `
     Компонент Alert
      ~~~js
      import Alert from 'n2o-framework/lib/components/snippets/Alert/Alert';
      
      <Alert
          label="Лейбл алерта"
          text="Текст алерта"
          visible={true}
          severity="info"
          closeButton={true}
          onDismiss={onDismiss}
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Без заголовка',
        () => (
            <Alert {...omit(props, 'label')} visible onDismiss={(e) => {}} />
        ),
        {
            info: {
                text: `
     Компонент Alert
      ~~~js
      import Alert from 'n2o-framework/lib/components/snippets/Alert/Alert';
      
      <Alert
          text="Текст алерта"
          visible={true}
          severity="info"
          closeButton={true}
          onDismiss={onDismiss}
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Без кнопки скрытия',
        () => (
            <Alert
                {...props}
                closeButton={false}
                visible
                onDismiss={(e) => {}}
            />
        ),
        {
            info: {
                text: `
     Компонент Alert
      ~~~js
      import Alert from 'n2o-framework/lib/components/snippets/Alert/Alert';
      
      <Alert
          label="Лейбл алерта"
          text="Текст алерта"
          visible={true}
          severity="info"
          closeButton={false}
          onDismiss={onDismiss}
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Цвета',
        () => (
            <>
                <Alert {...props} visible onDismiss={(e) => {}} />
                <Alert
                    {...props}
                    severity="danger"
                    visible
                    onDismiss={(e) => {}}
                />
                <Alert
                    {...props}
                    severity="warning"
                    visible
                    onDismiss={(e) => {}}
                />
                <Alert
                    {...props}
                    severity="success"
                    visible
                    onDismiss={(e) => {}}
                />
            </>
        ),
        {
            info: {
                text: `
     Компонент Alert
      ~~~js
      import Alert from 'n2o-framework/lib/components/snippets/Alert/Alert';
      
      <Alert
          {...props}
          color="info"
       />
       <Alert
          {...props}
          color="danger"
       />
       <Alert
          {...props}
          color="warning"
       />
       <Alert
          {...props}
          color="success"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Обновленный Alert',
        () => {
            const props = {
                label: 'Лейбл алерта',
                text: 'Текст алерта',
                details: 'Подробности алерта',
                severity: 'success',
                visible: true,
                closeButton: true,
                help: 'A little help',
            }

            return <Alert {...props} animate />
        },
        {
            info: {
                text: `
     Компонент Alert
      ~~~js
      import Alert from 'n2o-framework/lib/components/snippets/Alert/Alert';
      
      <Alert
          label="Лейбл алерта"
          details="Подробности алерта"
          text="Текст алерта"
          visible={true}
          severity="info"
          closeButton={true}
          onDismiss={onDismiss}
          help="A little help"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'С переносами текста',
        () => {
            const props = {
                label: 'Лейбл алерта',
                text: 'Текст алерта \n new line text',
                details: 'Подробности алерта',
                severity: 'success',
                visible: true,
                closeButton: true,
                help: 'A little help',
            }

            return <Alert {...props} animate />
        },
        {
            info: {
                text: `
     Компонент Alert
      ~~~js
      import Alert from 'n2o-framework/lib/components/snippets/Alert/Alert';
      
      <Alert
          label="Лейбл алерта"
          details="Подробности алерта"
          text='Текст алерта \n new line text'
          visible={true}
          severity="info"
          closeButton={true}
          onDismiss={onDismiss}
          help="A little help"
       />
      ~~~
      `,
            },
        },
    )
    .add(
        'Alert loader',
        () => <Alert loader text={undefined} severity="secondary" />,
        {
            info: {
                text: `
     Компонент Alert
      ~~~js
      import Alert from 'n2o-framework/lib/components/snippets/Alert/Alert';
      
      <Alert
          loader={true}
          severity="secondary"
       />
      ~~~
      `,
            },
        },
    )
    .add('Абсолютное и относительное позиционирование в виджете', () => {
        const widgetError = {
            meta: {
                alert: {
                    alertKey: 'Page_Form',
                    stacked: true,
                    messages: [
                        {
                            severity: 'success',
                            position: 'absolute',
                            text: 'Алерт с анимацией и абсолютным позиционированием',
                            animate: true,
                        },
                    ],
                },
            },
        }

        const widgetErrorStacked = {
            meta: {
                alert: {
                    alertKey: 'Page_Form',
                    stacked: true,
                    messages: [
                        {
                            severity: 'danger',
                            text: 'Алерт с анимацией и относительным позиционированием',
                            animate: true,
                        },
                    ],
                },
            },
        }

        fetchMock
            .restore()
            .post('begin:n2o/data/test', {
                status: 200,
                body: widgetError,
            })
            .post('begin:n2o/data/stacked-test', {
                status: 200,
                body: widgetErrorStacked,
            })
        return (
            <>
                <Factory
                    level={WIDGETS}
                    id="Page_Form"
                    {...widgetAlerts.Page_Form}
                />
            </>
        )
    })
    .add('Global alert', () => {
        const widgetError = {
            meta: {
                alert: {
                    alertKey: GLOBAL_KEY,
                    stacked: true,
                    messages: [
                        {
                            severity: 'success',
                            position: 'absolute',
                            text: 'Глобальный алерт',
                            animate: true,
                        },
                    ],
                },
            },
        }

        fetchMock.restore().post('begin:n2o/data/test', {
            status: 200,
            body: widgetError,
        })
        return (
            <>
                <GlobalAlerts />
                <Factory
                    level={WIDGETS}
                    id="Page_Form"
                    {...globalAlert.Page_Form}
                />
            </>
        )
    })
    .add('Создание через Factory', () => {
        const dt = {
            id: 'widget',
            src: 'Alert',
            label: 'Лейбл',
            text: 'Текст',
            details: 'Детали',
            severity: 'info',
            visible: true,
            closeButton: false,
        }
        return (
            <>
                <Factory level={SNIPPETS} id="widget" {...dt} />
            </>
        )
    })
