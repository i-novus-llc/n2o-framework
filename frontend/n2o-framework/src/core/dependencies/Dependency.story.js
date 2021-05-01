import React from 'react';
import { storiesOf } from '@storybook/react';

import { WIDGETS } from '../factory/factoryLevels';
import FieldDependency from './FieldDependency.meta';
import { parseUrl } from 'N2oStorybook/fetchMock';
import Factory from '../factory/Factory';

const stories = storiesOf('Функциональность/Зависимость между полями', module);

stories.add('Метаданные', () => {
  return (
    <Factory
      level={WIDGETS}
      id={'Page_Form'}
      {...FieldDependency['Page_Form']}
    />
  );
});
