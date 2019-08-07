import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import { withState } from '@dump247/storybook-state';
import withForm from 'N2oStorybook/decorators/withForm';
import Rating from './Rating';

import meta from './Raiting.meta';

const form = withForm({ src: 'Rating' });

const stories = storiesOf('Контролы/Рейтинг', module);

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);

stories
  .add('Компонент', () => {
    const props = {
      max: number('max', meta.max),
      half: boolean('half', meta.half),
      rating: number('rating', meta.rating),
      showTooltip: boolean('showTooltip', meta.showTooltip),
    };
    return <Rating {...props} />;
  })
  .add(
    'Метаданные',
    form(() => {
      return { ...meta, rating: 4 };
    })
  )
  .add('Тултип', () => {
    return <Rating {...meta} showTooltip={true} rating={2.5555555} />;
  })
  .add('Частичный выбор', () => {
    return (
      <Rating {...meta} showTooltip={true} half={true} rating={2.5555555} />
    );
  });
