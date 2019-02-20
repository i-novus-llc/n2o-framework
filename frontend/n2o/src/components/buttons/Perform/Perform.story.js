import React from 'react';
import { storiesOf } from '@storybook/react';
import { omit } from 'lodash';

import Perform from './Perform';
import MetaJson from './Perform.meta.json';

const stories = storiesOf('Кнопки', module);

stories.add('Выполнение redux action', () => <Perform {...omit(MetaJson, ['conditions'])} />);
