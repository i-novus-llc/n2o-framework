import React from 'react';

import map from 'lodash/map';
import get from 'lodash/get';
import isUndefined from 'lodash/isUndefined';

import Factory from '../../core/factory/Factory';
import { WIDGETS } from '../../core/factory/factoryLevels';

function RegionContent({ content, tabSubContentClass }) {
  const mapClassNames = {
    TabsRegion: tabSubContentClass,
  };

  return (
    <div>
      {map(content, (meta, index) => {
        const src = get(meta, 'src');
        const className =
          !isUndefined(get(mapClassNames, src)) && get(mapClassNames, src);

        return (
          <Factory
            level={WIDGETS}
            {...meta}
            key={index}
            className={className}
          />
        );
      })}
    </div>
  );
}

export default RegionContent;
