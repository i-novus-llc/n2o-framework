import React from 'react'
import { storiesOf } from '@storybook/react'
import { getStubData } from 'N2oStorybook/fetchMock'
import fetchMock from 'fetch-mock'

import Factory from '../../core/factory/Factory'
import { WIDGETS } from '../../core/factory/factoryLevels'

import metadata from './Invoke.meta.json'
// eslint-disable-next-line import/no-named-as-default
import Actions, { Actions as ActionsComponent } from './Actions'

const stories = storiesOf('Действия/POST запрос', module)
const urlPattern = 'begin:n2o/data'

stories.addParameters({
    info: {
        propTables: [ActionsComponent],
        propTablesExclude: [Actions],
    },
})

stories

    .add(
        'Компонент',
        () => {
            const toolbar = [
                {
                    buttons: [
                        {
                            id: 'testInvoke',
                            title: 'Пример Invoke',
                            actionId: 'invoke',
                            hint: 'Отправить invoke',
                        },
                    ],
                },
            ]
            return (
                <Actions
                    actions={metadata.Page_Table.actions}
                    toolbar={toolbar}
                    containerKey="actionExample"
                />
            )
        },
        {
            info: {
                text: `
      Компонент 'POST действие'
      ~~~js
      import Actions from 'n2o-framework/lib/components/actions/Actions';

      const toolbar = [
        {
          buttons: [
            {
              id: 'testInvoke',
              title: 'Пример Invoke',
              actionId: 'invoke',
              hint: 'Отправить invoke',
            },
          ],
        },
      ];

      <Actions
        actions={actions}
        toolbar={toolbar}
        containerKey="actionExample"
      />
      ~~~
      `,
            },
        },
    )
    .add('Метаданные', () => {
        fetchMock
            .restore()
            .get(urlPattern, url => getStubData(url))
            .post(urlPattern, () => ({
                meta: {
                    alert: {
                        alertKey: 'Page_Table',
                        messages: [
                            {
                                id: 'test',
                                severity: 'success',
                                text: 'Invoke прошел успешно',
                            },
                        ],
                    },
                },
            }))

        return (
            <Factory level={WIDGETS} {...metadata.Page_Table} id="Page_Table" />
        )
    })
