import React from 'react'
import { storiesOf } from '@storybook/react'
import withForm from 'N2oStorybook/decorators/withForm'

import Factory from '../../../core/factory/Factory'

import SnippetRating from './SnippetRating'
import meta from './SnippetRating.meta'

const form = withForm({ src: 'Rating' })

const stories = storiesOf('Контролы/Рейтинг', module)

stories.addParameters({
    info: {
        propTables: [SnippetRating],
        propTablesExclude: [Factory],
    },
})

stories
    .add(
        'Компонент',
        () => {
            const props = {
                max: meta.max,
                half: meta.half,
                rating: meta.rating,
                showTooltip: meta.showTooltip,
            }
            return <SnippetRating {...props} />
        },
        {
            info: {
                text: `
      Компонент 'Рейтинг'
      ~~~js
      import Rating from 'n2o-framework/lib/components/controls/Rating/Rating';
      
      <Rating
          max={5}
          half={false}
          rating={0}
          showTooltip={false}
      />
      ~~~
      `,
            },
        },
    )
    .add(
        'Метаданные',
        form(() => ({ ...meta, rating: 4 })),
    )
    .add(
        'Тултип',
        () => <SnippetRating {...meta} showTooltip rating={2.5555555} />,
        {
            info: {
                text: `
      Компонент 'Рейтинг'
      ~~~js
      import Rating from 'n2o-framework/lib/components/controls/Rating/Rating';
      
      <Rating
          max={5}
          half={false}
          rating={2.5555555}
          showTooltip={true}
      />
      ~~~
      `,
            },
        },
    )
    .add(
        'Частичный выбор',
        () => (
            <SnippetRating
                {...meta}
                showTooltip
                half
                rating={2.5555555}
            />
        ),
        {
            info: {
                text: `
      Компонент 'Рейтинг'
      ~~~js
      import Rating from 'n2o-framework/lib/components/controls/Rating/Rating';
      
      <Rating
          max={5}
          half={true}
          rating={2.5555555}
          showTooltip={true}
      />
      ~~~
      `,
            },
        },
    )
