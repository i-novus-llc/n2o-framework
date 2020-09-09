import React from 'react';

import map from 'lodash/map';

import Factory from '../../core/factory/Factory';
import { WIDGETS } from '../../core/factory/factoryLevels';

function RegionContent({ content }) {
  return (
    <div>
      {map(content, (meta, index) => (
        <Factory level={WIDGETS} {...meta} key={index} />
      ))}
    </div>
  );
}

export default RegionContent;
