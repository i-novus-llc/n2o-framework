import merge from 'deepmerge';

import controls from '../../components/controls';
import widgets from '../../components/widgets';
import regions from '../../components/regions';
import layouts from '../../components/layouts';
import headers from '../../components/widgets/Table/headers';
import cells from '../../components/widgets/Table/cells';
import fieldsets from '../../components/widgets/Form/fieldsets';
import fields from '../../components/widgets/Form/fields';
import actions from '../../impl/actions';

export const factories = {
  controls,
  widgets,
  regions,
  layouts,
  headers,
  cells,
  fieldsets,
  fields,
  actions
};

export default function createFactoryConfig(customConfig) {
  return merge(factories, customConfig);
}
