package org.BEH.Manager.Agent;

public class MemInfo {
	private long MemTotal;              // 系统可用物理内存总量
	private long MemFree;             // 系统空闲内物理内存总量 = HighFree+ LowFree
	private long MemUsed;				// 系统使用的物理内存
	private long Buffers;               // 系统分配但未被使用的 buffer 数量 ( 注 1)
	private long Cached;              // 系统分配但未被使用的 cache 数量 ( 注 1)
	private long SwapTotal;               // 总的交换内存大小
	private long SwapFree;              // 空闲的交换内存大小
	public long getMemTotal() {
		return MemTotal;
	}
	public void setMemTotal(long memTotal) {
		MemTotal = memTotal;
	}
	public long getMemFree() {
		return MemFree;
	}
	public void setMemFree(long memFree) {
		MemFree = memFree;
	}
	public long getBuffers() {
		return Buffers;
	}
	public void setBuffers(long buffers) {
		Buffers = buffers;
	}
	public long getCached() {
		return Cached;
	}
	public void setCached(long cached) {
		Cached = cached;
	}
	public long getSwapTotal() {
		return SwapTotal;
	}
	public void setSwapTotal(long swapTotal) {
		SwapTotal = swapTotal;
	}
	public long getSwapFree() {
		return SwapFree;
	}
	public void setSwapFree(long swapFree) {
		SwapFree = swapFree;
	}
	public long getMemUsed() {
		return MemUsed;
	}
	public void setMemUsed(long memUsed) {
		MemUsed = memUsed;
	}
	@Override
	public String toString() {
		return "MemInfo [MemTotal=" + MemTotal + ", MemFree=" + MemFree + ", MemUsed=" + MemUsed + ", Buffers="
				+ Buffers + ", Cached=" + Cached + ", SwapTotal=" + SwapTotal + ", SwapFree=" + SwapFree + "]";
	}
}
