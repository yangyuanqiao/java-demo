package main.jfinal.plugin.tablebind;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;

import main.jfinal.utils.StringUtils;

public class AutoTableBindPlugin extends ActiveRecordPlugin {
	private TableNameStyle tableNameStyle;
	private Logger logger = Logger.getLogger(getClass());

	public AutoTableBindPlugin(DataSource dataSource) {
		super(dataSource);
	}

	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, TableNameStyle tableNameStyle) {
		super(dataSourceProvider);
		this.tableNameStyle = tableNameStyle;
	}

	@Override
	public boolean start() {

		try {
			logger.info("--------init-----------table bind----------");
			List<Class> modelClasses = ClassSearcher.findClasses(Model.class);
			logger.debug("modelClasses.size  {}" + modelClasses.size());
			TableBind tb = null;
			for (Class modelClass : modelClasses) {
				tb = (TableBind) modelClass.getAnnotation(TableBind.class);
				if (tb == null) {
					this.addMapping(tableName(modelClass), modelClass);
					logger.debug("auto bindTable: addMapping({}, {}) tableName:" + tableName(modelClass)
							+ " modelClassName:" + modelClass.getName());
				} else {
					if (!StringUtils.isBlank(tb.pkName())) {
						this.addMapping(tb.tableName(), tb.pkName(), modelClass);
						logger.debug("auto bindTable: addMapping({}, {},{})tableName:" + new Object[] {
								tb.tableName() + " pkName:" + tb.pkName() + "modelClassName:" + modelClass.getName() });
					} else {
						this.addMapping(tb.tableName(), modelClass);
						logger.debug("auto bindTable: addMapping({}, {})" + "tableName" + tb.tableName()
								+ "modelClassName" + modelClass.getName());
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return super.start();
	}

	@Override
	public boolean stop() {
		return super.stop();
	}

	private String tableName(Class<?> clazz) {
		String tableName = clazz.getSimpleName();
		if (tableNameStyle == TableNameStyle.UP) {
			tableName = tableName.toUpperCase();
		} else if (tableNameStyle == TableNameStyle.LOWER) {
			tableName = tableName.toLowerCase();
		} else {
			// tableName = StringUtils.firstCharToLowerCase(tableName);
		}
		return tableName;
	}
}