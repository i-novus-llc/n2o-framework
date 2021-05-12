import React, { useState } from 'react'
import { storiesOf } from '@storybook/react'

import NumberPicker from './NumberPicker'
import NumberPickerJson from './NumberPicker.meta.json'

const stories = storiesOf('Контролы/NumberPicker', module)

stories.addParameters({
    info: {
        propTables: [NumberPicker],
    },
})

function NumberPickerComponent(props) {
    const [value, setValue] = useState(props.value)

    return <NumberPicker {...props} value={value} onChange={setValue} />
}

stories.add(
    'Компонент',
    () => <NumberPickerComponent {...NumberPickerJson} />,
    {
        info: {
            text: `
      Компонент 'Контрол NumberPicker'
      ~~~js
      import NumberPicker from 'n2o-framework/lib/components/controls/NumberPicker/NumberPicker';
      
      <NumberPicker
          visible={true}
          value={0}
          step={2}
          max={100}
          min={0}
      />
      ~~~
      `,
        },
    },
)
