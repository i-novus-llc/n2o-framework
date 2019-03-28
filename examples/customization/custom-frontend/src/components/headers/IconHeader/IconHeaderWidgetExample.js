import React from 'react';
import Table from 'n2o/lib/components/widgets/Table/Table';
import IconHeader from './IconHeader';
import TextCell from 'n2o/lib/components/widgets/Table/cells/TextCell/TextCell';

class InputCellWidgetExample extends React.Component {
    render() {
        const tableProps = {
            headers: [
                {
                    id: 'id_1',
                    component: IconHeader,
                    label: "Пример IconHeader",
                    "icon": "fa fa-plus"
                }
            ],
            cells: [
                {
                    id: 'id_1',
                    component: TextCell
                }
            ],
            datasource: [
                {
                    "id_1": "Тестовое значение"
                }
            ]
        };


        return (
            <div>
                <Table
                    {...this.props}
                    headers={tableProps.headers}
                    cells={tableProps.cells}
                    datasource={tableProps.datasource}
                />
            </div>
        );
    }
}

export default InputCellWidgetExample;