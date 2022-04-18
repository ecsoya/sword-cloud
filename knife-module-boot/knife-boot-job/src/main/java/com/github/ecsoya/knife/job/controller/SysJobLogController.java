package com.github.ecsoya.knife.job.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ecsoya.knife.common.core.utils.poi.ExcelUtil;
import com.github.ecsoya.knife.common.core.web.controller.BaseController;
import com.github.ecsoya.knife.common.core.web.domain.AjaxResult;
import com.github.ecsoya.knife.common.core.web.page.TableDataInfo;
import com.github.ecsoya.knife.common.log.annotation.Log;
import com.github.ecsoya.knife.common.log.enums.BusinessType;
import com.github.ecsoya.knife.common.security.annotation.RequiresPermissions;
import com.github.ecsoya.knife.job.domain.SysJobLog;
import com.github.ecsoya.knife.job.service.ISysJobLogService;

/**
 * 调度日志操作处理
 * 
 * @author AngryRED (angryred@qq.com)
 */
@RestController
@RequestMapping("/job/log")
public class SysJobLogController extends BaseController {
	@Autowired
	private ISysJobLogService jobLogService;

	/**
	 * 查询定时任务调度日志列表
	 */
	@RequiresPermissions("monitor:job:list")
	@GetMapping("/list")
	public TableDataInfo list(SysJobLog sysJobLog) {
		startPage();
		List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
		return getDataTable(list);
	}

	/**
	 * 导出定时任务调度日志列表
	 */
	@RequiresPermissions("monitor:job:export")
	@Log(title = "任务调度日志", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	public void export(HttpServletResponse response, SysJobLog sysJobLog) {
		List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
		ExcelUtil<SysJobLog> util = new ExcelUtil<SysJobLog>(SysJobLog.class);
		util.exportExcel(response, list, "调度日志");
	}

	/**
	 * 根据调度编号获取详细信息
	 */
	@RequiresPermissions("monitor:job:query")
	@GetMapping(value = "/{configId}")
	public AjaxResult getInfo(@PathVariable Long jobLogId) {
		return AjaxResult.success(jobLogService.selectJobLogById(jobLogId));
	}

	/**
	 * 删除定时任务调度日志
	 */
	@RequiresPermissions("monitor:job:remove")
	@Log(title = "定时任务调度日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{jobLogIds}")
	public AjaxResult remove(@PathVariable Long[] jobLogIds) {
		return toAjax(jobLogService.deleteJobLogByIds(jobLogIds));
	}

	/**
	 * 清空定时任务调度日志
	 */
	@RequiresPermissions("monitor:job:remove")
	@Log(title = "调度日志", businessType = BusinessType.CLEAN)
	@DeleteMapping("/clean")
	public AjaxResult clean() {
		jobLogService.cleanJobLog();
		return AjaxResult.success();
	}
}
