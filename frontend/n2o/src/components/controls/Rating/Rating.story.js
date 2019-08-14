import React from 'react';
import { storiesOf } from '@storybook/react';

import withForm from 'N2oStorybook/decorators/withForm';
import Rating from './Rating';

import meta from './Raiting.meta';
import Factory from '../../../core/factory/Factory';

const form = withForm({ src: 'Rating' });

const stories = storiesOf('Контролы/Рейтинг', module);

stories.addParameters({
  info: {
    propTables: [Rating],
    propTablesExclude: [Factory],
  },
});

stories
  .add(
    'Компонент',
    () => {
      const props = {
        max: meta.max,
        half: meta.half,
        rating: meta.rating,
        showTooltip: meta.showTooltip,
      };
      return <Rating {...props} />;
    },
    {
      info: {
        text: `
      Компонент 'Рейтинг'
      ~~~js
      import Rating from 'n2o/lib/components/controls/Rating/Rating';
      
      <Rating
          max={5}
          half={false}
          rating={0}
          showTooltip={false}
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'Метаданные',
    form(() => {
      return { ...meta, rating: 4 };
    })
  )
  .add(
    'Тултип',
    () => {
      return <Rating {...meta} showTooltip={true} rating={2.5555555} />;
    },
    {
      info: {
        text: `
      Компонент 'Рейтинг'
      ~~~js
      import Rating from 'n2o/lib/components/controls/Rating/Rating';
      
      <Rating
          max={5}
          half={false}
          rating={2.5555555}
          showTooltip={true}
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'Частичный выбор',
    () => {
      return (
        <Rating {...meta} showTooltip={true} half={true} rating={2.5555555} />
      );
    },
    {
      info: {
        text: `
      Компонент 'Рейтинг'
      ~~~js
      import Rating from 'n2o/lib/components/controls/Rating/Rating';
      
      <Rating
          max={5}
          half={true}
          rating={2.5555555}
          showTooltip={true}
      />
      ~~~
      `,
      },
    }
  );
