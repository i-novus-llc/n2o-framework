/**
 * Created by emamoshin on 05.09.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';
import { Regions } from 'local-n2o';
import _ from 'lodash';
import Widget from '../../widgets/Widget';
import { getWidgetId } from '../../../tools/helpers';

class Pills extends Regions.Tabs {

    render() {
        return (
            <div className="mb-2">
                <div class="nav flex-column nav-pills">
                    {this.props.metadata.containers.map(cnt =>
                        <a className={(cnt.id == this.state.active.id ? 'nav-link active' : 'nav-link')}
                           onClick={(e) => { e.preventDefault(); this.setActive(cnt); } }
                           href={`#${cnt.id}`}>{cnt.icon ? `<span class="${cnt.icon}"></span>` : null}{cnt.name || cnt.id}</a>
                    )}
                </div>
                <div class="tab-content">
                    <div class="tab-pane active">
                        {this.regionWidgets}
                    </div>
                </div>
            </div>
        );
    }

}

export default Pills;


