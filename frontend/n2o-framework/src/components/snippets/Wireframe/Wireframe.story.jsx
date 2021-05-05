import React from 'react'
import { storiesOf } from '@storybook/react'
import Card from 'reactstrap/lib/Card'
import CardBody from 'reactstrap/lib/CardBody'

import Factory from '../../../core/factory/Factory'
import { SNIPPETS } from '../../../core/factory/factoryLevels'

import Wireframe from './Wireframe'

const stories = storiesOf('UI Компоненты/Визуальная заглушка', module)

stories.addParameters({
    info: {
        propTablesExclude: [Card, CardBody],
    },
})

stories
    .add(
        'Компонент',
        () => {
            const props = {
                className: 'n2o',
                title: 'Текст визуальной заглушки',
            }

            return (
                <Card>
                    <CardBody
                        style={{
                            padding: '60px',
                            position: 'relative',
                        }}
                    >
                        <Wireframe {...props} />
                    </CardBody>
                </Card>
            )
        },
        {
            info: {
                text: `
      Компонент 'Визуальная заглушка'
      ~~~js
      import Wireframe from 'n2o-framework/lib/components/snippets/Wireframe/Wireframe;
      
      <Wireframe className="n2o" title="Текст визуальной заглушки" />
      ~~~
      `,
            },
        },
    )
    .add(
        'Создание через Factory',
        () => {
            const dt = {
                id: 'uniqId',
                src: 'Wireframe',
                className: 'n2o',
                title: 'Текст визуальной заглушки',
            }

            return (
                <>
                    <Factory level={SNIPPETS} id="uniqId" {...dt} />
                </>
            )
        },
        {
            info: {
                text: `
      Компонент 'Визуальная заглушка'
      ~~~js
      import Factory from 'n2o-framework/lib/core/factory/Factory';
      
      <Factory level={SNIPPETS} id="uniqid" {...wireframeProps} />
      ~~~
      `,
            },
        },
    )
